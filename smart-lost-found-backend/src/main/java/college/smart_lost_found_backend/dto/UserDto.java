package college.smart_lost_found_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDto {

    private Long id;
    private String userName;
    private String token;
    private String email;
    private String roles;
    private String address;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String password;
    private Boolean emailVerified;
    private String verificationToken;

    public UserDto(Long id,
                   String userName,
                   String email,
                   String roles,
                   Boolean emailVerified,
                   String phoneNumber,
                   String firstName,
                   String lastName) {
        this.id=id;
        this.userName=userName;
        this.email=email;
        this.roles=roles;
        this.emailVerified=emailVerified;
        this.phoneNumber=phoneNumber;
        this.firstName=firstName;
        this.lastName=lastName;
    }
}
