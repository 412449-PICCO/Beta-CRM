package ar.edu.utn.frc.tup.lc.iii.omnichannel.dtos;

import ar.edu.utn.frc.tup.lc.iii.omnichannel.entities.ChannelType;
import ar.edu.utn.frc.tup.lc.iii.omnichannel.entities.Direction;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionDto {
    private UUID id;
    private UUID tenantId;

    @NotNull(message = "El canal es requerido")
    private ChannelType channel;

    @NotNull(message = "La dirección es requerida")
    private Direction direction;

    private String state;
    private UUID agentId;
    private UUID leadId;
    private UUID queueId;
    private Instant startedAt;
    private Instant closedAt;
    private Long duration;
    private String notes;
    private String metadata;
    private Instant createdAt;
    private Instant updatedAt;
}

