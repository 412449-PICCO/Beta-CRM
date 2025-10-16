package ar.edu.utn.frc.tup.lc.iii.omnichannel.entities;

import ar.edu.utn.frc.tup.lc.iii.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "interactions", indexes = {
    @Index(name = "idx_interaction_tenant", columnList = "tenant_id"),
    @Index(name = "idx_interaction_agent", columnList = "agent_id"),
    @Index(name = "idx_interaction_lead", columnList = "lead_id"),
    @Index(name = "idx_interaction_state", columnList = "state")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interaction extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelType channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;

    @Column(nullable = false)
    private String state; // queued, active, ended

    private UUID agentId;

    private UUID leadId;

    private UUID queueId;

    private Instant startedAt;

    private Instant closedAt;

    private Long duration; // duración en segundos

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String metadata; // JSON con datos adicionales del canal
}

