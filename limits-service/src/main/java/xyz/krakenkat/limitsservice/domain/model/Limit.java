package xyz.krakenkat.limitsservice.domain.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Limit {
    private int minimum;
    private int maximum;
}
