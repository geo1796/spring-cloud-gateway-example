package com.toutoleron.formapp.auth.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class SignUpRequest {

    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    private List<String> roles;

}
