package com.dvdstore.service;

import com.dvdstore.model.Disc;

import java.util.List;

public interface DiscService {
    List<Disc> findCurrentUserDiscs(int id);
    Disc findById(int id);
}
