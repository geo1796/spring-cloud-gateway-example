package com.toutoleron.formapp.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "annuaire", url = "https://production.api-annuaire-sante.fr")
@Component
public interface AnnuaireProxy {

    @GetMapping("/professionnel_de_santes")
    public Page<?> doctors(@RequestHeader("Authorization") String authorization, Pageable pageable);

}
