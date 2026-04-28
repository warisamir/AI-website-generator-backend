package com.rockhardy.lovable.dto.file;

public record FileNode(
        String path
) {
    @Override
    public String toString(){
        return path;
    }
}
