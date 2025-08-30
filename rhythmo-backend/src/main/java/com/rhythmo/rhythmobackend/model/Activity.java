package com.rhythmo.rhythmobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "activity")
public class Activity {
    public enum Type {COVER, STUDIO, DJ_SET}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50, message = "Max length for title is 50 symbols")
    @Column(nullable = false)
    @NonNull
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private Type type;

    @URL(message = "Activity URL should be valid.")
    @Column
    @NonNull
    private String url;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NonNull
    @JsonIgnore
    private User author;

    @Size(max = 100, message = "Max length for description is 100 symbols")
    @Column
    private String description;

    @CreationTimestamp
    @Column(nullable = false)
    private Date creationTime;
}
