package com.cony.roomy.batch.config;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.List;

/**
 * 기존의 JpaItemWriter 에서 Entity만 처리할 수 있는 한계점을 보완
 * 리스트를 받아 n개의 데이터를 write
 */

public class JpaItemListWriter<T> extends JpaItemWriter<List<T>> {
    private JpaItemWriter<T> jpaItemWriter;

    public JpaItemListWriter(JpaItemWriter<T> jpaItemWriter) {
        this.jpaItemWriter = jpaItemWriter;
    }

    @Override
    public void write(Chunk<? extends List<T>> items) {
        Chunk<T> totalList = new Chunk<>();

        for(List<T> list: items) {
            totalList.addAll(list);
        }

        jpaItemWriter.write(totalList);
    }
}
