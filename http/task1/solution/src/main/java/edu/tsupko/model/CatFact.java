package edu.tsupko.model;

public record CatFact(
    String id,
    String text,
    String type,
    String user,
    Integer upvotes
) {
}
