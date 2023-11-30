package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int op=0;
        operacaoBancoDeDados opBancoDeDados = new operacaoBancoDeDados();
        ConexaoPostgres con = new ConexaoPostgres();
        do {
            System.out.println("Sistema bibliotecario!!!" +
                    "\n\nLIVROS!!!"+
                    "\n(1)-Adicionar" +
                    "\n(2)-Remover" +
                    "\n(3)-Listar todos os livros" +
                    "\n(4)-Pesquisar" +
                    "\n(5)-Atualizar" +
                    "\n\nFUNCIONARIOS!!!" +
                    "\n(6)-Adicionar" +
                    "\n(7)-Pesquisar" +
                    "\n(8)-Deletar" +
                    "\n\nALUNOS!!!" +
                    "\n(9)-Adicionar" +
                    "\n(10)-Pesquisar" +
                    "\n(11)-Deletar" +
                    "\n\nSERVIÇOS!!!" +
                    "\n(12)-Emprestimo" +
                    "\n(13)-Devolução" +
                    "\n(14)-Listar emprestimos" +
                    "\nSua opção: ");
            op=sc.nextInt();
            try(Connection conexao = obterConexao()) {
                switch (op){
                    case 1:
                        opBancoDeDados.addNovoLivro(conexao);
                        break;

                    case 2:
                        System.out.println("Digite o título do livro a ser excluído:");
                        sc.nextLine();
                        String tituloParaExclusao = sc.nextLine();
                        System.out.println("Digite o autor do livro a ser excluído:");
                        String autorParaExclusao = sc.nextLine();
                        System.out.println("Digite o ano de lançamento do livro a ser excluído:");
                        int anoLancamentoParaExclusao = sc.nextInt();
                        operacaoBancoDeDados.excluirLivro(conexao, tituloParaExclusao, autorParaExclusao, anoLancamentoParaExclusao);
                        break;
                    case 3:
                        operacaoBancoDeDados.listarLivros(conexao);
                        break;
                    case 4:
                        System.out.println("Escolha o parametro de pesquisa: " +
                                "\n(1)-ID" +
                                "\n(2)-Titulo" +
                                "\n(3)-Autor" +
                                "\n(4)Editora");
                        int opPesquisa = sc.nextInt();
                        switch (opPesquisa){
                            case 1:
                                System.out.println("Digite o ID do livro para pesquisa:");
                                int idParaPesquisa =sc.nextInt();
                                operacaoBancoDeDados.pesquisarLivroPorId(conexao, idParaPesquisa);
                            break;
                            case 2:
                                System.out.println("Digite o nome do livro para pesquisa");
                                sc.nextLine();
                                String nomeParaPesquisa =sc.nextLine();
                                operacaoBancoDeDados.pesquisarLivroPorNome(conexao, nomeParaPesquisa);
                            break;
                            case 3:
                                System.out.println("Digite o autor para pesquisa");
                                sc.nextLine();
                                String autorParaPesquisa = sc.nextLine();
                                operacaoBancoDeDados.pesquisarLivroPorAutor(conexao,autorParaPesquisa);

                            break;
                            case 4:
                                System.out.println("Digite a editora para pesquisa");
                                sc.nextLine();
                                String editoraParaPesquisa = sc.nextLine();
                                operacaoBancoDeDados.pesquisarLivroPorEditora(conexao,editoraParaPesquisa);
                            break;
                        }
                    break;

                    case 5:
                        operacaoBancoDeDados.editarLivro(conexao);
                        break;
                    case 6:
                        operacaoBancoDeDados.cadastrarFuncionario(conexao);
                        break;
                    case 7:
                        operacaoBancoDeDados.pesquisarFuncionario(conexao);
                        break;
                    case 8:
                        operacaoBancoDeDados.deletarFuncionario(conexao);
                        break;
                    case 9:
                        operacaoBancoDeDados.adicionarAluno(conexao);
                        break;
                    case 10:
                        operacaoBancoDeDados.pesquisarAlunoPorCPF(conexao);

                        break;
                    case 11:
                        operacaoBancoDeDados.deletarAluno(conexao);
                        break;
                    case 12:
                        operacaoBancoDeDados.realizarEmprestimo(conexao);
                        break;
                    case 13:
                        System.out.println("Informe o codigo de emprestimo para devolução");
                        int codEmprestimo = sc.nextInt();
                        operacaoBancoDeDados.realizarDevolucao(conexao,codEmprestimo);
                        break;
                    case 14:
                        operacaoBancoDeDados.imprimirTodosEmprestimos(conexao);
                        break;
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
        }while(1>0);
    }
    private static Connection obterConexao() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/biblioteca";
        String usuario = "postgres";
        String senha = "merg01";
        return DriverManager.getConnection(url, usuario, senha);
    }
}