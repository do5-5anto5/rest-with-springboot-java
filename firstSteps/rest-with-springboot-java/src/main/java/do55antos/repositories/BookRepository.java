package do55antos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import do55antos.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{}
