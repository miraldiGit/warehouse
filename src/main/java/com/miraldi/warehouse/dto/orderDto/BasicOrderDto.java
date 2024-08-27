package com.miraldi.warehouse.dto.orderDto;

import com.miraldi.warehouse.utils.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BasicOrderDto {
    private Long orderNumber;
    private LocalDate submittedDate;
    private LocalDate deadlineDate;
    private Status status;
    private String declinedReason;
    private BigDecimal orderTotalPrice;
}
