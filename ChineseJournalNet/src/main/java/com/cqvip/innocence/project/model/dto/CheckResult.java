package com.cqvip.innocence.project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 01
 * @date 2022-01-12 14:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckResult {
    private Boolean limited;

    private String msg;
}
