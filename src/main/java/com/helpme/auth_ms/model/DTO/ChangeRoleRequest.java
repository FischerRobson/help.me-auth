package com.helpme.auth_ms.model.DTO;

import com.helpme.auth_ms.model.Roles;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeRoleRequest {
    private String role;
}
