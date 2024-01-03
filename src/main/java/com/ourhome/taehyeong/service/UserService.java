package com.ourhome.taehyeong.service;

import com.ourhome.taehyeong.entities.Privacy;
import com.ourhome.taehyeong.entities.User;
import com.ourhome.taehyeong.entities.dto.PayloadDto;
import com.ourhome.taehyeong.entities.dto.PrivacyDto;
import com.ourhome.taehyeong.entities.enums.Role;
import com.ourhome.taehyeong.repository.PrivacyRepository;
import com.ourhome.taehyeong.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrivacyRepository privacyRepository;

    @Autowired
    private AuthService authService;

    public void deleteUserByUsername(String name) {
        Optional<User> found = userRepository.findUserByUsername(name);

        if (found.isPresent())
            userRepository.delete(found.get());
        else
            throw new UsernameNotFoundException("user does not exist");
    }

    public void updatePrivacy(HttpServletRequest request, PrivacyDto updatePrivacyDto) {
        String jwt = request.getHeader("Authorization");
        PayloadDto payloadDto = authService.findUsernameByToken(jwt);

        String username = updatePrivacyDto.getUsername();
        Optional<User> found = userRepository.findUserByUsername(username);
        if (found.isEmpty()) throw new UsernameNotFoundException("username : " + username + "does not exist.");
        Role foundRole = found.get().getRole();

        if (foundRole == Role.ROLE_ADMIN && payloadDto.getRole().equals(Role.ROLE_OPERATOR.toString()))
            throw new AccessDeniedException("Access denied.");

        Privacy privacy = found.get().getPrivacy();
        privacy.setUniversity(updatePrivacyDto.getUniversity());
        privacy.setBirth(updatePrivacyDto.getBirth());
        privacy.setName(updatePrivacyDto.getName());
        privacy.setAddress(updatePrivacyDto.getAddress());
        privacyRepository.save(privacy);
    }

    public List<Privacy> getPrivacy() {
        List<Privacy> result = privacyRepository.findAll();
        for (Privacy x : result) {
            System.out.println(x.getBirth());
        }
        return result;
    }

    // 찾을 기준, 기준 인자, 정렬여부, 갯수
    public List<Privacy> getPrivacyByParam(String columnName, String arg) {

        if (!columnName.isEmpty() && !arg.isEmpty()) {
            if (columnName.equals("name")) {
                return privacyRepository.findPrivacyByName(arg);
            }
            if (columnName.equals("university")) {
                return privacyRepository.findPrivacyByUniversity(arg);
            }
            if (columnName.equals("address")) {
                return  privacyRepository.findPrivacyByAddress(arg);
            }
            if (columnName.equals("birthyear")) {
                String startYear = arg + "-1-1";
                String endYear = arg + "-12-31";
                return  privacyRepository.findPrivacyByBirthBetween(Date.valueOf(startYear), Date.valueOf(endYear));
            }
        }
        throw new IllegalArgumentException("invalid parameter");
    }

    public List<Privacy> getPrivacyAll() {
        return privacyRepository.findAll();
    }
}
