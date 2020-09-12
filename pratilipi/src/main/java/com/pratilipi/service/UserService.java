package com.pratilipi.service;

import com.pratilipi.dao.UsersDao;
import com.pratilipi.dao.model.Users;
import com.pratilipi.model.UserLoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UsersDao usersDao;

    public static final String ENCRYPT_KEY="PRODKEYPRODKEY12";

    public Users getUserDetails(String username) throws IOException {
        try {
            Users users = usersDao.getUserDetails(username);
            return users;
        } catch (EmptyResultDataAccessException e) {
            throw new IOException("Username does not exist");
        }
    }

    public void checkUserDetails(UserLoginRequest loginRequest) throws IOException {
        //get user data based on username
        Users users = getUserDetails(loginRequest.getUsername());
        //encrypt password and check for validity
        String encryptedPassword = encrypt(ENCRYPT_KEY, loginRequest.getPassword());
        if (!encryptedPassword.equals(users.getPassword())) {
            throw new IOException("Password mismatch");
        }
    }

    public void signUp(UserLoginRequest loginRequest) throws IOException {
        //get user data based on username
        try {
            Users users = getUserDetails(loginRequest.getUsername());
            if (Objects.nonNull(users))
                throw new IOException("Username already exists, please give different one");
        } catch (IOException e) {
            if (e.getMessage().contains("does not")) {
                //encrypt password and check for validity
                String encryptedPassword = encrypt(ENCRYPT_KEY, loginRequest.getPassword());

                //save in DB
                Users newUser = new Users();
                newUser.setUsername(loginRequest.getUsername());
                newUser.setPassword(encryptedPassword);
                usersDao.save(newUser);
            } else
                throw e;
        }
    }

    public static String encrypt(String key, String data) {
        byte[] encrypted = new byte[0];
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            encrypted = cipher.doFinal(data.getBytes());
            return new String(Base64.encodeBase64(encrypted));
        } catch (Exception e) {
            log.error("Error in encrypting", e);
        }
        return "";
    }
}
