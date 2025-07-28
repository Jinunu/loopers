package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import static com.loopers.support.error.ErrorType.BAD_REQUEST;

@Entity
@Table(name = "users")
public class UserModel extends BaseEntity {

    private String userId;
    private String gender;
    private String email;
    private String birthDate;

    public UserModel(String userId, String email, String birthDate, String gender){
        if (!isValidUserId(userId)) {
            throw new CoreException(BAD_REQUEST, "영문 및 숫자 10자 이내로 입력해 주세요.");
        }
        if (gender == null || gender.isBlank()){
            throw new CoreException(BAD_REQUEST, "성별은 필수 값 입니다.");
        }
        if (!isEmail(email)){
            throw new CoreException(BAD_REQUEST, "이메일 형식이 올바르지 않습니다.");
        }

        if (!isValidBirthDate(birthDate)){
            throw new CoreException(BAD_REQUEST, "생년월일 형식에 맞지 않습니다.");
        }

        this.userId = userId;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;

    }

    public UserModel() {
    }

    public String getUserId() {
        return userId;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    private boolean isValidUserId(String userId){
        String userIdRegex = "^[a-zA-Z0-9]{1,10}$";
        return userId != null && userId.matches(userIdRegex);
    }

    private boolean isValidBirthDate(String birthDate) {
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate.parse(birthDate, formatter);

        }catch (DateTimeParseException parseException){
            return false;
        }
        return true;
    }

    public boolean containsSpecialChar(String str){
        for (char c: str.toCharArray()) {
            if (!Character.isLetterOrDigit(c)){
                return true;
            }
        }
        return false;
    }

    public boolean isEmail(String email){
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (email == null || email.isBlank()) {
            return false;
        }
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

}
