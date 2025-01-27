package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Introspected;

import jakarta.validation.constraints.NotBlank;

@Introspected
public class Book {
    @NonNull
    @NotBlank
    private final String isbn;

    @NonNull
    @NotBlank
    private final String name;

    public Book(@NonNull @NotBlank String isbn, @NonNull @NotBlank String name) {
        this.isbn = isbn;
        this.name = name;
    }

    @NonNull
    public String getIsbn() {
        return isbn;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!isbn.equals(book.isbn)) return false;
        return name.equals(book.name);
    }

    @Override
    public int hashCode() {
        int result = isbn.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}