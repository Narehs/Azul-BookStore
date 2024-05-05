package org.bookstore.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    default Page<Book> searchBooksUsingCriteria(String searchKey, Pageable pageable) {
        return findAll((Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + searchKey.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("authors").get("firstName")), "%" + searchKey.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("authors").get("lastName")), "%" + searchKey.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("genres").get("name")), "%" + searchKey.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("isbn")), "%" + searchKey.toLowerCase() + "%")
            );
        }, pageable);
    }
}
