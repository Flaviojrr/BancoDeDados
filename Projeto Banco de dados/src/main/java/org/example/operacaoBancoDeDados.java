package org.example;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Date;
public class operacaoBancoDeDados {
    private static final Scanner sc = new Scanner(System.in);
    public static void addNovoLivro(Connection conexao) {
        try {
            // Obtém o próximo valor da sequência para ser utilizado como o id
            int id = obterProximoValorSequencia(conexao, "sua_tabela_id_seq");

            // Inserir dados em uma tabela
            String insertSQL = "INSERT INTO livros (id, titulo, autor, ano_lancamento, genero, editora) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(insertSQL)) {
                // Substituir os valores dos parâmetros pelos dados que você deseja inserir
                preparedStatement.setInt(1, id);
                System.out.println("Titulo: ");
                preparedStatement.setString(2, sc.nextLine()); // Título
                System.out.println("Autor: ");
                preparedStatement.setString(3, sc.nextLine()); // Autor
                System.out.println("Ano de lançamento: ");
                preparedStatement.setInt(4, sc.nextInt()); // Ano de lançamento
                System.out.println("Genero: ");
                sc.nextLine();
                preparedStatement.setString(5, sc.nextLine()); // Gênero
                System.out.println("Editora: ");
                preparedStatement.setString(6, sc.nextLine()); // Editora

                // Executar a inserção
                int addLivro = preparedStatement.executeUpdate();
                // Exibir mensagem de sucesso
                System.out.println("\nLivro adicionado com sucesso!!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int obterProximoValorSequencia(Connection conexao, String nomeSequencia) throws SQLException {
        String query = "SELECT nextval(?)";
        try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
            preparedStatement.setString(1, nomeSequencia);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        throw new SQLException("Falha ao obter o próximo valor da sequência.");
    }

    public static void excluirLivro(Connection conexao, String titulo, String autor, int anoLancamento) {
        try {
            String query = "DELETE FROM livros WHERE LOWER(titulo) = LOWER(?) AND LOWER(autor) = LOWER(?) AND ano_lancamento = ?";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                preparedStatement.setString(1, titulo);
                preparedStatement.setString(2, autor);
                preparedStatement.setInt(3, anoLancamento);

                int linhasAfetadas = preparedStatement.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Livro excluído com sucesso!");
                } else {
                    System.out.println("Nenhum livro encontrado com as informações fornecidas.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void pesquisarLivroPorNome(Connection conexao, String nome) {
        try {
            String query = "SELECT * FROM livros WHERE LOWER(titulo) LIKE LOWER(?)";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + nome + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    boolean encontrouLivro = false;

                    while (resultSet.next()) {
                        encontrouLivro = true;
                        imprimirInformacoesLivro(resultSet);
                        System.out.println("-----");
                    }

                    if (!encontrouLivro) {
                        System.out.println("Nenhum livro encontrado com o título informado.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void pesquisarLivroPorId(Connection conexao, int id) {
        try {
            String query = "SELECT * FROM livros WHERE id = ?";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                preparedStatement.setInt(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        imprimirInformacoesLivro(resultSet);
                    } else {
                        System.out.println("Nenhum livro encontrado com o ID informado.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void pesquisarLivroPorAutor(Connection conexao, String autor) {
        try {
            String query = "SELECT * FROM livros WHERE LOWER(autor) LIKE LOWER(?)";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + autor + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    boolean encontrouLivro = false;

                    while (resultSet.next()) {
                        encontrouLivro = true;
                        imprimirInformacoesLivro(resultSet);
                        System.out.println("-----");
                    }

                    if (!encontrouLivro) {
                        System.out.println("Nenhum livro encontrado com o autor informado.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void pesquisarLivroPorEditora(Connection conexao, String editora) {
        try {
            String query = "SELECT * FROM livros WHERE LOWER(editora) LIKE LOWER(?)";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + editora + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    boolean encontrouLivro = false;

                    while (resultSet.next()) {
                        encontrouLivro = true;
                        imprimirInformacoesLivro(resultSet);
                        System.out.println("-----");
                    }

                    if (!encontrouLivro) {
                        System.out.println("Nenhum livro encontrado com a editora informado.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void editarLivro(Connection conexao) {
        try {
            System.out.println("Digite o ID do livro que deseja editar: ");
            int idLivro = sc.nextInt();

            // Verifica se o livro com o ID fornecido existe
            if (livroExiste(conexao, idLivro)) {
                System.out.println("----------------"+
                        "\nEscolha o que deseja editar:" +
                        "\n(1)-Titulo" +
                        "\n(2)-Autor" +
                        "\n(3)-Ano de Laçamento" +
                        "\n(4)-Genero" +
                        "\n(5)-Editora" +
                        "\n(6)-Sinopse" +
                        "\nSua escolha:" +
                        "\n----------------");

                int escolha = sc.nextInt();
                sc.nextLine();  // Limpar o buffer

                String colunaAtualizar = "";
                String novoValor = "";

                switch (escolha) {
                    case 1:
                        colunaAtualizar = "titulo";
                        System.out.println("Novo Título: ");
                        novoValor = sc.nextLine();
                        break;
                    case 2:
                        colunaAtualizar = "autor";
                        System.out.println("Novo Autor: ");
                        novoValor = sc.nextLine();
                        break;
                    case 3:
                        colunaAtualizar = "ano_lancamento";
                        System.out.println("Novo Ano de Lançamento: ");
                        novoValor = sc.nextLine();
                        break;
                    case 4:
                        colunaAtualizar = "genero";
                        System.out.println("Novo Gênero: ");
                        novoValor = sc.nextLine();
                        break;
                    case 5:
                        colunaAtualizar = "editora";
                        System.out.println("Nova Editora: ");
                        novoValor = sc.nextLine();
                        break;
                    case 6:
                        colunaAtualizar = "sinopse";
                        System.out.println("Nova Sinopse: ");
                        novoValor = sc.nextLine();
                        break;
                    default:
                        System.out.println("Opção inválida.");
                        return;
                }

                // Atualizar no banco de dados
                String updateSQL = "UPDATE livros SET " + colunaAtualizar + " = ? WHERE id = ?";
                try (PreparedStatement preparedStatement = conexao.prepareStatement(updateSQL)) {
                    preparedStatement.setString(1, novoValor);
                    preparedStatement.setInt(2, idLivro);

                    int linhasAfetadas = preparedStatement.executeUpdate();

                    if (linhasAfetadas > 0) {
                        System.out.println("Livro atualizado com sucesso!");
                    } else {
                        System.out.println("Falha ao atualizar o livro.");
                    }
                }
            } else {
                System.out.println("Livro não encontrado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean livroExiste(Connection conexao, int idLivro) throws SQLException {
        String query = "SELECT id FROM livros WHERE id = ?";
        try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
            preparedStatement.setInt(1, idLivro);

            try (var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
    public static void listarLivros(Connection conexao) {
        try {
            String query = "SELECT * FROM livros";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String titulo = resultSet.getString("titulo");
                    String autor = resultSet.getString("autor");
                    int anoLancamento = resultSet.getInt("ano_lancamento");
                    String genero = resultSet.getString("genero");
                    String estado = resultSet.getString("estado");
                    String editora = resultSet.getString("editora");
                    String sinopse = resultSet.getString("sinopse");

                    System.out.println("----------------"
                            +"\nID: " + id +
                            "\nTítulo: " + titulo +
                            "\nAutor: " + autor +
                            "\nAno de Lançamento: " + anoLancamento +
                            "\nGênero: " + genero +
                            "\nEstado: " + estado+
                            "\nEditora: " + editora +
                            "\nSinopse: " + sinopse +
                            "\n----------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void adicionarAluno(Connection conexao) {
        try {
            System.out.println("Digite o CPF do aluno: ");
            String cpf = sc.next();

            // Verifica se o aluno já está cadastrado
            if (!alunoExiste(conexao, cpf)) {
                System.out.println("Digite o nome do aluno: ");
                String nome = sc.next();

                // Insere o aluno na tabela ALUNOS
                String insertSQL = "INSERT INTO ALUNOS (cpf, nome) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = conexao.prepareStatement(insertSQL)) {
                    preparedStatement.setString(1, cpf);
                    preparedStatement.setString(2, nome);

                    int linhasAfetadas = preparedStatement.executeUpdate();

                    if (linhasAfetadas > 0) {
                        System.out.println("Aluno cadastrado com sucesso!");
                    } else {
                        System.out.println("Falha ao cadastrar o aluno.");
                    }
                }
            } else {
                System.out.println("Aluno já cadastrado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void pesquisarAlunoPorCPF(Connection conexao) {
        try {
            System.out.println("Digite o CPF do aluno: ");
            String cpf = sc.next();

            // Verifica se o aluno está cadastrado
            if (alunoExiste(conexao, cpf)) {
                String query = "SELECT * FROM ALUNOS WHERE cpf = ?";
                try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                    preparedStatement.setString(1, cpf);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            imprimirInformacoesAluno(resultSet);
                        }
                    }
                }
            } else {
                System.out.println("Aluno não encontrado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean alunoExiste(Connection conexao, String cpf) throws SQLException {
        String query = "SELECT cpf FROM ALUNOS WHERE cpf = ?";
        try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
            preparedStatement.setString(1, cpf);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private static void imprimirInformacoesAluno(ResultSet resultSet) throws SQLException {
        System.out.println("CPF: " + resultSet.getString("cpf") +
                "\nNome: " + resultSet.getString("nome"));
    }
    public static void realizarEmprestimo(Connection conexao) {
        try {
            System.out.println("Digite o CPF do funcionário: ");
            String cpfFuncionario = sc.next();
            System.out.println("Digite a senha do funcionário: ");
            String senhaFuncionario = sc.next();

            // Verifica se o funcionário está cadastrado e a senha está correta
            if (verificarCredenciaisFuncionario(conexao, cpfFuncionario, senhaFuncionario)) {
                System.out.println("Digite o CPF do aluno: ");
                String cpfAluno = sc.next();

                // Verifica se o aluno está cadastrado
                if (alunoExiste(conexao, cpfAluno)) {
                    System.out.println("Digite o ID do livro: ");
                    int idLivro = sc.nextInt();

                    // Verifica se o livro está cadastrado
                    if (livroExiste(conexao, idLivro)) {
                        // Gera um número de empréstimo entre 10 e 99
                        int codEmprestimo = gerarNumeroAleatorio(10, 99);

                        // Gera as datas de empréstimo e devolução com um intervalo de duas semanas
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date dataEmprestimo = new Date();
                        Date dataDevolucao = adicionarDias(dataEmprestimo, 14);

                        // Converte as datas para java.sql.Date
                        java.sql.Date sqlDataEmprestimo = new java.sql.Date(dataEmprestimo.getTime());
                        java.sql.Date sqlDataDevolucao = new java.sql.Date(dataDevolucao.getTime());

                        // Registra o empréstimo na tabela EMPRESTIMO
                        String insertSQL = "INSERT INTO EMPRESTIMO (cod_emprestimo, data_emprestimo, data_devolucao, cpf_aluno, id_livro, estado_livro, cpf_funcionario) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = conexao.prepareStatement(insertSQL)) {
                            preparedStatement.setInt(1, codEmprestimo);
                            preparedStatement.setDate(2, sqlDataEmprestimo);
                            preparedStatement.setDate(3, sqlDataDevolucao);
                            preparedStatement.setString(4, cpfAluno);
                            preparedStatement.setInt(5, idLivro);

                            // Define o estado inicial do livro como "emprestado"
                            preparedStatement.setString(6, "emprestado");

                            // Adiciona o CPF do funcionário à coluna correspondente
                            preparedStatement.setString(7, cpfFuncionario);

                            int linhasAfetadas = preparedStatement.executeUpdate();

                            if (linhasAfetadas > 0) {
                                System.out.println("Empréstimo registrado com sucesso!");
                            } else {
                                System.out.println("Falha ao registrar o empréstimo.");
                            }
                        }
                    } else {
                        System.out.println("Livro não encontrado.");
                    }
                } else {
                    System.out.println("Aluno não encontrado.");
                }
            } else {
                System.out.println("CPF ou senha incorretos. Não é possível realizar o empréstimo.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void atualizarEstadoLivro(Connection conexao, int idLivro, String novoEstado) throws SQLException {
        String updateSQL = "UPDATE livros SET estado = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = conexao.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, novoEstado);
            preparedStatement.setInt(2, idLivro);

            int linhasAfetadas = preparedStatement.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Estado do livro atualizado para " + novoEstado);
            } else {
                System.out.println("Falha ao atualizar o estado do livro.");
            }
        }
    }


// ...

    private static boolean verificarCredenciaisFuncionario(Connection conexao, String cpf, String senha) throws SQLException {
        String query = "SELECT * FROM FUNCIONARIO WHERE CPF = ? AND SENHA = ?";
        try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
            preparedStatement.setString(1, cpf);
            preparedStatement.setString(2, senha);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
    private static int gerarNumeroAleatorio(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private static Date adicionarDias(Date data, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.DAY_OF_MONTH, dias);
        return (Date) calendar.getTime();
    }
    public static void imprimirTodosEmprestimos(Connection conexao) {
        try {
            String query = "SELECT E.cod_emprestimo, E.data_emprestimo, E.data_devolucao, "
                    + "A.nome AS nome_aluno, A.cpf AS cpf_aluno, "
                    + "F.nome AS nome_funcionario, F.cpf AS cpf_funcionario, "
                    + "L.titulo AS titulo_livro, L.autor AS autor_livro, L.editora AS editora_livro "
                    + "FROM EMPRESTIMO E "
                    + "JOIN ALUNOS A ON E.cpf_aluno = A.cpf "
                    + "JOIN FUNCIONARIO F ON E.cpf_funcionario = F.cpf "
                    + "JOIN LIVROS L ON E.id_livro = L.id";

            try (PreparedStatement preparedStatement = conexao.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int codEmprestimo = resultSet.getInt("cod_emprestimo");
                    Date dataEmprestimo = resultSet.getDate("data_emprestimo");
                    Date dataDevolucao = resultSet.getDate("data_devolucao");
                    String nomeAluno = resultSet.getString("nome_aluno");
                    String cpfAluno = resultSet.getString("cpf_aluno");
                    String nomeFuncionario = resultSet.getString("nome_funcionario");
                    String cpfFuncionario = resultSet.getString("cpf_funcionario");
                    String tituloLivro = resultSet.getString("titulo_livro");
                    String autorLivro = resultSet.getString("autor_livro");
                    String editoraLivro = resultSet.getString("editora_livro");

                    System.out.println("----------------"+
                            "\nCódigo de Empréstimo: " + codEmprestimo +
                            "\nData de Empréstimo: " + dataEmprestimo +
                            "\nData de Devolução: " + dataDevolucao +
                            "\nNome do Aluno: " + nomeAluno +
                            "\nCPF do Aluno: " + cpfAluno +
                            "\nNome do Funcionário: " + nomeFuncionario +
                            "\nCPF do Funcionário: " + cpfFuncionario +
                            "\nTítulo do Livro: " + tituloLivro +
                            "\nAutor do Livro: " + autorLivro +
                            "\nEditora do Livro: " + editoraLivro +
                            "\n----------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public static void realizarDevolucao(Connection conexao, int codEmprestimo) {
        try {
            // Atualizar a data de devolução na tabela EMPRESTIMO
            String updateSQL = "UPDATE EMPRESTIMO SET data_devolucao = CURRENT_DATE WHERE cod_emprestimo = ?";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(updateSQL)) {
                preparedStatement.setInt(1, codEmprestimo);

                int linhasAfetadas = preparedStatement.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Devolução realizada com sucesso!");

                    // Excluir o registro do empréstimo após a devolução
                    String deleteSQL = "DELETE FROM EMPRESTIMO WHERE cod_emprestimo = ?";
                    try (PreparedStatement deleteStatement = conexao.prepareStatement(deleteSQL)) {
                        deleteStatement.setInt(1, codEmprestimo);
                        deleteStatement.executeUpdate();
                        System.out.println("Registro de empréstimo excluído do banco de dados.");

                        // Obtém o ID do livro associado ao empréstimo
                        int idLivro = obterIdLivroPorCodEmprestimo(conexao, codEmprestimo);

                        // Atualiza o estado do livro para "disponível"
                        atualizarEstadoLivro(conexao, idLivro, "disponível");
                    }
                } else {
                    System.out.println("Nenhum empréstimo encontrado com o código informado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int obterIdLivroPorCodEmprestimo(Connection conexao, int codEmprestimo) {
        try {
            String query = "SELECT id_livro FROM EMPRESTIMO WHERE cod_emprestimo = ?";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                preparedStatement.setInt(1, codEmprestimo);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id_livro");
                    } else {
                        System.out.println("Nenhum livro encontrado com o código de empréstimo informado.");
                        return -1;  // Retorna um valor indicando que nenhum livro foi encontrado
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;  // Retorna um valor indicando erro
        }
    }


    private static void imprimirInformacoesLivro(ResultSet resultSet) throws SQLException {
        System.out.println("----------------"+
                "\nID: " + resultSet.getInt("id") +
                "\nTítulo: " + resultSet.getString("titulo") +
                "\nAutor: " + resultSet.getString("autor") +
                "\nAno de Lançamento: " + resultSet.getInt("ano_lancamento") +
                "\nGênero: " + resultSet.getString("genero") +
                "\nEditora: " + resultSet.getString("editora") +
                "\nEstado: " + resultSet.getString("estado") +
                "\nSinopse: " + resultSet.getString("sinopse")+
                "\n----------------");
    }
    public static void cadastrarFuncionario(Connection conexao) {
        try {
            System.out.println("Informe o CPF do funcionário: ");
            String cpf = sc.next();

            // Verificar se o CPF já existe na tabela
            if (verificarExistenciaFuncionario(conexao, cpf)) {
                System.out.println("CPF já cadastrado. Não é possível cadastrar novamente.");
                return;
            }

            System.out.println("Informe o nome do funcionário: ");
            String nome = sc.next();

            System.out.println("Informe a senha do funcionário: ");
            String senha = sc.next();

            // Inserir o funcionário na tabela FUNCIONARIO
            String insertSQL = "INSERT INTO FUNCIONARIO (CPF, Nome, Senha) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, cpf);
                preparedStatement.setString(2, nome);
                preparedStatement.setString(3, senha);

                int linhasAfetadas = preparedStatement.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Funcionário cadastrado com sucesso!");
                } else {
                    System.out.println("Falha ao cadastrar funcionário.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para verificar se o funcionário já existe
    private static boolean verificarExistenciaFuncionario(Connection conexao, String cpf) throws SQLException {
        String query = "SELECT COUNT(*) FROM FUNCIONARIO WHERE CPF = ?";
        try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
            preparedStatement.setString(1, cpf);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }
    public static void pesquisarFuncionario(Connection conexao) {
        try {
            System.out.println("Informe o CPF do funcionário que deseja pesquisar: ");
            String cpf = sc.next();

            // Pesquisar o funcionário na tabela FUNCIONARIO
            String query = "SELECT * FROM FUNCIONARIO WHERE CPF = ?";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                preparedStatement.setString(1, cpf);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        System.out.println("Funcionário encontrado:");
                        imprimirInformacoesFuncionario(resultSet);
                    } else {
                        System.out.println("Nenhum funcionário encontrado com o CPF informado.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para imprimir informações do funcionário
    private static void imprimirInformacoesFuncionario(ResultSet resultSet) throws SQLException {
        System.out.println("----------------"+"\nCPF: " + resultSet.getString("CPF"));
        System.out.println("Nome: " + resultSet.getString("Nome"));
        System.out.println("Senha: " + resultSet.getString("Senha")+
                "\n----------------");
    }
    public static void deletarFuncionario(Connection conexao) {
        try {
            System.out.println("Informe o CPF do funcionário que deseja deletar: ");
            String cpf = sc.next();

            // Deletar o funcionário na tabela FUNCIONARIO
            String deleteSQL = "DELETE FROM FUNCIONARIO WHERE CPF = ?";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(deleteSQL)) {
                preparedStatement.setString(1, cpf);

                int linhasAfetadas = preparedStatement.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Funcionário deletado com sucesso!");
                } else {
                    System.out.println("Nenhum funcionário encontrado com o CPF informado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deletarAluno(Connection conexao) {
        try {
            System.out.println("Informe o CPF do aluno que deseja deletar: ");
            String cpf = sc.next();

            // Deletar o aluno na tabela ALUNO
            String deleteSQL = "DELETE FROM ALUNO WHERE CPF = ?";
            try (PreparedStatement preparedStatement = conexao.prepareStatement(deleteSQL)) {
                preparedStatement.setString(1, cpf);

                int linhasAfetadas = preparedStatement.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Aluno deletado com sucesso!");
                } else {
                    System.out.println("Nenhum aluno encontrado com o CPF informado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}