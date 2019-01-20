package com.uxpsystems.assignment.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(notes = "First Name of the user")
    @Column(name = "user_name", nullable=false,unique=true)
    @NotEmpty(message = "error.user.name.length")
    @Length(max = 50,  message = "error.name.length")
    private String userName;

    @ApiModelProperty(notes = "First Name of the user")
    @Column(name = "first_name", nullable=false)
    @NotEmpty(message = "error.first.name.length")
    @Length(max = 50,  message = "error.name.length")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "error.last.name.length")
    @Length(max = 50,  message = "error.name.length")
    private String lastName;

    @ApiModelProperty(notes = "Email of the user")
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "error.email.email")
    @NotEmpty(message = "error.email.empty")
    @Length(max = 80,  message = "error.email.length")
    private String email;

    @Column(name = "password")
    @Transient
    private String password;

    @ApiModelProperty(notes = "Confirmation token received over registered email of the user")
    @Column(name = "confirmation_token")
    private String confirmationToken;

    @ApiModelProperty(notes = "Active/DeActive Status of the user")
    @Column(name = "status", nullable=false)
    @Length(max = 50,  message = "error.name.length")
    private String status;

    @ApiModelProperty(notes = "Registration date of the user")
    @Column(name="date")
    private Date regDate;

    @ApiModelProperty(notes = "Registration modification date of the user")
    @Column(name="date", insertable=false, updatable = false)
    private Date updateDate;
}
