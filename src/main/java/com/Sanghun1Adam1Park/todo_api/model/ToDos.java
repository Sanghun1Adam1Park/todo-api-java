package com.Sanghun1Adam1Park.todo_api.model;

import com.Sanghun1Adam1Park.todo_api.enums.PriorityLevel;
import com.Sanghun1Adam1Park.todo_api.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter @Setter @ToString
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "uk_todos_id_owner", columnNames = {"id", "owner_id"})
})
@Check(constraints = "priority in ('CRITICAL','HIGH','MODERATE','LOW') and status in ('IDLE','PENDING','COMPLETE')")
public class ToDos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_todos_owner"))
    private Users owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private PriorityLevel priority = PriorityLevel.MODERATE;

    @Column(nullable = false)
    private String item;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Status status = Status.IDLE;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @Column
    private LocalDate deadline;

    @Column
    private Instant finishedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
