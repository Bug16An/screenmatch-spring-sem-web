package br.com.alura.screematch2.principal;


import br.com.alura.screematch2.model.*;
import br.com.alura.screematch2.repository.SerieRepository;
import br.com.alura.screematch2.service.ConsumoApi;
import br.com.alura.screematch2.service.ConverterDados;



import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);

    private ConsumoApi consumo = new ConsumoApi();

    private ConverterDados conversor = new ConverterDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    String omdbApiKey = System.getenv("OMDB_API_KEY");
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }


    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Busca séries.
                    2 - Busca episódios.
                    3 - Lista de séries buscadas.
                                    
                    0 - Sair.
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série que deseja buscar: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + omdbApiKey);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }
    
    private void buscarEpisodioPorSerie() {
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + omdbApiKey);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void listarSeriesBuscadas() {

        List<Serie> series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}

//        System.out.println("Digite o nome da série");
//        var nomeSerie = leitura.nextLine();
//        var json = consumo.obterDados(ENDERECO + nomeSerie.replace( " ", "+") + API_KEY);
//        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
//        System.out.println(dados);


//        for(int i = 0; i < dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporadas = temporadas.get(i).episodios();
//            for(int j = 0; j < episodiosTemporadas.size(); j++){
//                System.out.println(episodiosTemporadas.get(j).titulo());
//            }
//        }  Pega os nomes dos episódios de 1 temporada da série, porem é melhor ser feito no lambda

//       temporadas.forEach(t -> t.episodios().forEach(e-> System.out.println(e.titulo())));
//expressão em lambda que realiza a mesma função do for anterior, de forma mais simples

//        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
//
//        System.out.println("\n Top 5 Melhores episódeos");
//
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenação " + e))
//                .limit(5)
//                .peek(e -> System.out.println("Limite " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Mapeamento " + e))
//                .forEach(System.out::println);
//
//        List<Episodio> episodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream()
//                .map(d -> new Episodio(t.numero(), d))
//                ).collect(Collectors.toList());
//        episodios.forEach(System.out::println);
//
//        System.out.println("Digite um trecho do titulo do episódio: ");
//        var trechoTitulo = leitura.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                //se caso quisesse fazer uma lista de episódios com o mesmo trecho de nome
//                //List<Episodio> episodiosBuscados = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                //Coleta os episódios filtrados em uma nova lista usando
//                // collect(Collectors.toList()). e não usaria a linah de baixo.
//                .findFirst();
//        // Se você quiser apenas a primeira ocorrência, use esse em vez do anterior.
//
//        //if (!episodiosBuscados.isEmpty()) {
//        //    System.out.println("Episódios encontrados:");
//        //    for (Episodio episodio : episodiosBuscados) {
//        //        System.out.println("Temporada: " + episodio.getTemporada() + ", Título: " + episodio.getTitulo());
//        //    }
//        //} else {
//        //    System.out.println("Nenhum episódio encontrado com o trecho de título informado.");
//        //} if se caso optar pela lista.
//
//        if (episodioBuscado.isPresent()){
//            System.out.println("Episódio encontrado! ");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//        } else{
//            System.out.println("Episódio não encontrado! ");
//        }
//
//        System.out.println("A partir de qual ano você deseja ver os episódios? ");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1,1);
////Isso realiza uma altereção no formato para o formato que estamos acostumados no Brasil.
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                       "Temporada: " + e.getTemporada()+
//                               "Episódio: " + e.getTitulo() +
//                               "Data de lançamento: " + e.getDataLancamento().format(formatador)
//                ));
//
//        Map<Integer, Double> avaliacoesPorTemporadas = episodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.groupingBy(Episodio::getTemporada,
//                        Collectors.averagingDouble(Episodio::getAvaliacao)));
//        System.out.println(avaliacoesPorTemporadas);
//
//        DoubleSummaryStatistics est =episodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
//        System.out.println("Média: " + est.getAverage());
//        System.out.println("Melhor Episódio: " + est.getMax());
//        System.out.println("Pior Episódio: " + est.getMin());
//        System.out.println("Quantidade: " + est.getCount());
//                                                            1

