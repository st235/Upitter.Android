package com.github.sasd97.upitter.models.skeletons;

/**
 * Created by Alex on 10.06.2016.
 */
public interface HeaderSkeleton {

    String getHeader();
    boolean isHeader();
    void setIsHeader(boolean isHeader);

    int getSectionManager();
    void setSectionManager(int manager);

    int getSectionFirstPosition();
    void setSectionFirstPosition(int position);
}
