package ar.edu.utn.frc.tup.lc.iii.directory.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTenantRequest {

    @NotBlank(message = "El nombre del CRM es requerido")
    private String name;

    private Map<String, Object> settings;
}

