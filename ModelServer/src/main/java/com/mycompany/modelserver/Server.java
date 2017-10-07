/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.modelserver;

import org.tensorflow.*;
import java.util.*;
import java.nio.file.*;
import java.nio.*;   
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author pedro
 */
@RestController
@EnableAutoConfiguration
public class Server {
    
    private static String MODEL_DIR = "/home/pedro/wcc-hackathon-2017/model/";
    
    public static void main(String... args) {
        SpringApplication.run(Server.class, args);
    }
    
    @PostMapping("/")
    public String loadModel() {
        SavedModelBundle bundle = SavedModelBundle.load(MODEL_DIR, "serve");
        final float[][] resultArray; 
       try (Graph g = bundle.graph()) {
            try (
                Session s = bundle.session(); Tensor result = s.runner().feed("data", data)
                ) {
            }
        }
        return "TensorFlow version: " + TensorFlow.version();
    }
    
}
