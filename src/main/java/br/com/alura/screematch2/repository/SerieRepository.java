package br.com.alura.screematch2.repository;

import br.com.alura.screematch2.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {
}
