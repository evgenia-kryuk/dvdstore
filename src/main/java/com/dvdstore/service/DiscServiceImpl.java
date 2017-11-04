package com.dvdstore.service;

import com.dvdstore.model.Disc;
import com.dvdstore.repository.DiscRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscServiceImpl implements DiscService {
    @Autowired
    private DiscRepository discRepo;

    @Override
    public List<Disc> findCurrentUserDiscs(int id) {
        return this.discRepo.findCurrentUserOwnDiscs(id);
    }

    @Override
    public Disc findById(int id) {
        return this.discRepo.findById(id);
    }
}
