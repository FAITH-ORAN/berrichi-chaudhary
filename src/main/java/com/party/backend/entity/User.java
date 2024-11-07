package com.party.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "users",indexes = {
        @Index(name = "idx_user_email", columnList = "email")
})
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Le pseudo est obligatoire")
    private String pseudo;

    @Column(nullable = false)
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre, et un caractère spécial"
    )
    private String password;

    private Integer age;

    private String interests;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Location location;

    /* Relation avec ProfileRating (One-to-Many), avec FetchType.LAZY
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<ProfileRating> ratings;

    // Relation avec Participation (One-to-Many), avec FetchType.LAZY
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Participation> participations;


    // Relation avec Message (One-to-Many pour l’envoi et la réception), avec FetchType.LAZY
    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private List<Message> receivedMessages;

    // Relation avec Proposition (One-to-Many), avec FetchType.LAZY
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Proposition> propositions;

    // Relation avec Vote (One-to-Many), avec FetchType.LAZY
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Vote> votes;*/
}
