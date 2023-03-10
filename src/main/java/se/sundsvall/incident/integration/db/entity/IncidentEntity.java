package se.sundsvall.incident.integration.db.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.sundsvall.incident.dto.Category;
import se.sundsvall.incident.dto.Status;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Errands")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(setterPrefix = "with")
@Getter
@Setter
public class IncidentEntity implements Serializable {

    @Id
    @Column(name = "IncidentId")
    private String incidentId;

    @Column(name = "externalCaseId")
    private String externalCaseId;

    @Column(name = "PersonId")
    private String personID;

    @Column(name = "Created")
    private String created;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Email")
    private String email;

    @Column(name = "ContactMethod")
    private String contactMethod;

    @Column(name = "Updated")
    private String updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "Category")
    private Category category;

    @Column(name = "Description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private Status status;

    @Column(name = "MapCoordinates")
    private String mapCoordinates;

    @Column(name = "Feedback")
    private String feedback;
}
