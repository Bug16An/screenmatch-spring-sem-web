package br.com.alura.screematch2;

import br.com.alura.screematch2.model.DadosSerie;
import br.com.alura.screematch2.service.ConsumoApi;
import br.com.alura.screematch2.service.ConverterDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Screematch2Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Screematch2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=3a0ce277");
		System.out.println(json);
		ConverterDados conversor = new ConverterDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
		
		
		
	}
}
