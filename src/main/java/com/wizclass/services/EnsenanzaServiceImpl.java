package com.wizclass.services;

import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.wizclass.model.Ensenanza;

@Service
public class EnsenanzaServiceImpl implements Comparator<Ensenanza>{
	@Override
    public int compare(Ensenanza e1, Ensenanza e2) {
        return e1.getId().compareTo(e2.getId());
    }
}