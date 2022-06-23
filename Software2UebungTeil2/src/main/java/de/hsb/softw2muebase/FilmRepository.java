package de.hsb.softw2muebase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
interface FilmRepository extends JpaRepository<Film, Integer> {
}

