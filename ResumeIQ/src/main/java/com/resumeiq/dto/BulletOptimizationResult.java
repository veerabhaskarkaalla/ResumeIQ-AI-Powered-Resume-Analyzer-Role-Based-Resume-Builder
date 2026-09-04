package com.resumeiq.dto;

import java.util.ArrayList;
import java.util.List;

public class BulletOptimizationResult {

    private List<BulletRewrite> rewrites =
            new ArrayList<>();


    public BulletOptimizationResult() {
    }


    public List<BulletRewrite> getRewrites() {
        return rewrites;
    }


    public void setRewrites(
            List<BulletRewrite> rewrites) {

        this.rewrites = rewrites;
    }
}