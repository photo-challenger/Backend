package com.photoChallenger.tripture.domain.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPaySessionDto {
    private String tid;
    private String order_id;
    private Long item_id;
}
