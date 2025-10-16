package ar.edu.utn.frc.tup.lc.iii.directory.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TenantDto {

    private UUID id;

    private String name;

    private boolean active;

    private Map<String, Object> settings;

    private Instant createdAt;
}

