package com.urllookup.controller;

import com.urllookup.model.URL;
import com.urllookup.model.Response;
import com.urllookup.respository.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.urllookup.Constants.*;

@RestController
public class AuthNController {
    @Autowired
    URLRepository urlRepository;

    @GetMapping("/v1/urlinfo/{url}")
    Response authenticateAccess (@PathVariable String url) {
        Optional<URL> urls;

        try {
            urls = urlRepository.findById(url);
        } catch(Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR);
        }
        if(urls.isEmpty()) {
            return new Response(HttpStatus.OK.value(), SUCCESS);
        }
        return new Response(HttpStatus.FORBIDDEN.value(), FAILURE);
    }
}
