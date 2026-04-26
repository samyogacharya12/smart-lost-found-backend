package college.smart_lost_found_backend.dto;

import lombok.Data;

@Data
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

    public UserDto(Long id,
                   String userName,
                   String email,
                   String roles) {
        this.id=id;
        this.userName=userName;
        this.email=email;
        this.roles=roles;
    }

    public UserDto(String firstName,
                   String lastName,
                   String address,
                   String phoneNumber,
                   Long userId,
                   String userName) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.address=address;
        this.phoneNumber=phoneNumber;
        this.id=userId;
        this.userName=userName;
    }
}
