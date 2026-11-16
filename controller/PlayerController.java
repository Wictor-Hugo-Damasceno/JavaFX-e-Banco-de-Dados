
package controller;

import model.Database;
import model.Player;
import model.PlayerRepositorio;

import javafx.fxml.FXML;
import javafx.application.Platform;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

public class PlayerController {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNickName;

    @FXML
    private TextField txtForca;

    @FXML
    private TextField txtHp;

    @FXML
    private TableView<Player> tabelaPlayers;

    @FXML
    private TableColumn<Player, Integer> colId;

    @FXML
    private TableColumn<Player, String> colNickName;

    @FXML
    private TableColumn<Player, Integer> colForca;

    @FXML
    private TableColumn<Player, Integer> colHp;

    @FXML
    private Label lblStatus;

    private Database database;
    private PlayerRepositorio playerRepositorio;

    private ObservableList<Player> listaPlayers;

    @FXML
    public void initialize() {

        try {
            database = new Database("rpg.db");

            playerRepositorio = new PlayerRepositorio(database);

            listaPlayers = FXCollections.observableArrayList();

            configurarTabela();

            carregarPlayers();

            configurarSelecao();

            lblStatus.setText("Sistema iniciado com sucesso.");

        } catch (Exception e) {
            mostrarErro("Erro ao iniciar o sistema: " + e.getMessage());
        }
    }

    private void configurarTabela() {

        colId.setCellValueFactory(
            new PropertyValueFactory<>("id")
        );

        colNickName.setCellValueFactory(
            new PropertyValueFactory<>("nickName")
        );

        colForca.setCellValueFactory(
            new PropertyValueFactory<>("forca")
        );

        colHp.setCellValueFactory(
            new PropertyValueFactory<>("hp")
        );

        tabelaPlayers.setItems(listaPlayers);
    }

    private void configurarSelecao() {

        tabelaPlayers.getSelectionModel()
            .selectedItemProperty()
            .addListener((observable, antigo, selecionado) -> {

                if (selecionado != null) {

                    txtId.setText(
                        String.valueOf(selecionado.getId())
                    );

                    txtNickName.setText(
                        selecionado.getNickName()
                    );

                    txtForca.setText(
                        String.valueOf(selecionado.getForca())
                    );

                    txtHp.setText(
                        String.valueOf(selecionado.getHp())
                    );

                    lblStatus.setText(
                        "Player selecionado: " +
                        selecionado.getNickName()
                    );
                }
            });
    }

    private void carregarPlayers() {

        try {

            listaPlayers.setAll(
                playerRepositorio.loadAll()
            );

            lblStatus.setText(
                listaPlayers.size() + " Player(s) carregado(s)."
            );

        } catch (Exception e) {
            mostrarErro("Erro ao carregar Players: " + e.getMessage());
        }
    }

    @FXML
    private void cadastrarPlayer() {

        try {

            String nickName = txtNickName.getText().trim();

            if (nickName.isEmpty()) {
                mostrarAviso("Digite um nickname.");
                return;
            }

            int forca = Integer.parseInt(
                txtForca.getText().trim()
            );

            int hp = Integer.parseInt(
                txtHp.getText().trim()
            );

            if (forca < 0 || hp < 0) {
                mostrarAviso("Força e HP não podem ser negativos.");
                return;
            }

            Player player = new Player(
                nickName,
                forca,
                hp
            );

            playerRepositorio.create(player);

            carregarPlayers();

            limparCampos();

            lblStatus.setText(
                "Player cadastrado com sucesso!"
            );

            mostrarSucesso("Player cadastrado com sucesso!");

        } catch (NumberFormatException e) {

            mostrarAviso(
                "Força e HP devem conter números inteiros."
            );

        } catch (Exception e) {

            mostrarErro(
                "Erro ao cadastrar Player: " + e.getMessage()
            );
        }
    }

    @FXML
    private void atualizarPlayer() {

        try {

            if (txtId.getText().isEmpty()) {
                mostrarAviso("Selecione um Player na tabela.");
                return;
            }

            String nickName = txtNickName.getText().trim();

            if (nickName.isEmpty()) {
                mostrarAviso("Digite um nickname.");
                return;
            }

            int id = Integer.parseInt(txtId.getText());

            int forca = Integer.parseInt(
                txtForca.getText().trim()
            );

            int hp = Integer.parseInt(
                txtHp.getText().trim()
            );

            if (forca < 0 || hp < 0) {
                mostrarAviso("Força e HP não podem ser negativos.");
                return;
            }

            Player player = playerRepositorio.loadFromId(id);

            if (player == null) {
                mostrarAviso("Player não encontrado no banco.");
                carregarPlayers();
                limparCampos();
                return;
            }

            player.setNickName(nickName);
            player.setForca(forca);
            player.setHp(hp);

            playerRepositorio.update(player);

            carregarPlayers();

            limparCampos();

            mostrarSucesso("Player atualizado com sucesso!");

        } catch (NumberFormatException e) {

            mostrarAviso(
                "Força e HP devem conter números inteiros."
            );

        } catch (Exception e) {

            mostrarErro(
                "Erro ao atualizar Player: " + e.getMessage()
            );
        }
    }

    @FXML
    private void excluirPlayer() {

        try {

            if (txtId.getText().isEmpty()) {
                mostrarAviso("Selecione um Player na tabela.");
                return;
            }

            int id = Integer.parseInt(txtId.getText());

            Player player = playerRepositorio.loadFromId(id);

            if (player == null) {
                mostrarAviso("Player não encontrado.");
                carregarPlayers();
                limparCampos();
                return;
            }

            Alert confirmacao = new Alert(
                Alert.AlertType.CONFIRMATION
            );

            confirmacao.setTitle("Excluir Player");

            confirmacao.setHeaderText(
                "Deseja realmente excluir este Player?"
            );

            confirmacao.setContentText(
                "Nickname: " + player.getNickName()
            );

            Optional<ButtonType> resultado =
                confirmacao.showAndWait();

            if (resultado.isPresent() &&
                resultado.get() == ButtonType.OK) {

                playerRepositorio.delete(player);

                carregarPlayers();

                limparCampos();

                mostrarSucesso(
                    "Player excluído com sucesso!"
                );
            }

        } catch (Exception e) {

            mostrarErro(
                "Erro ao excluir Player: " + e.getMessage()
            );
        }
    }

    @FXML
    private void limparCampos() {

        txtId.clear();
        txtNickName.clear();
        txtForca.clear();
        txtHp.clear();

        tabelaPlayers.getSelectionModel().clearSelection();

        lblStatus.setText("Campos limpos.");

    }

    @FXML
    private void fecharAplicacao() {
        Platform.exit();
    }

    @FXML
    private void sobre() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Sobre");
        alert.setHeaderText("Sistema de cadastro de Players");
        alert.setContentText(
            "Aplicação desenvolvida utilizando JavaFX, SQLite e ORMLite."
        );

        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        alert.showAndWait();
    }

    private void mostrarAviso(String mensagem) {

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        alert.showAndWait();
    }

    private void mostrarErro(String mensagem) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Erro");
        alert.setHeaderText("Ocorreu um problema.");
        alert.setContentText(mensagem);

        alert.showAndWait();
    }
}
