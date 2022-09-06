package tech.outspace.papershare.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaRowDto {
    private String id;
    private String name;
    private Long paperUsage;
    private Long userUsage;
}
