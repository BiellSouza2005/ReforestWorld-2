package RW.controller_dao;

import RW.connection.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexaoDAO {
    
    //private Conexao conexao;
    
    public void cadastrarUsuario(
            String nome, String email, String senha, String dt_nascimento, String sexo, String cpf, String code ) throws SQLException {      
        var conexao = new Conexao().conectar();
        var p = conexao.prepareStatement(
            "INSERT INTO users(id, nome, email, senha, dt_nascimento, sexo, cpf, cod_verificacao) values (null,?,?,?,?,?,?,?);");
        p.setString(1, nome);
        p.setString(2, email);
        p.setString(3, senha);
        p.setString(4, dt_nascimento);
        p.setString(5, sexo);
        p.setString(6, cpf);
        p.setString(7, code);
        p.execute();
        p.close();
        conexao.close();               
    }
    
    public void cadastrarEvento(String nome, String local, String data, String descricao, int id_usuario) throws SQLException{
        var conexao = new Conexao().conectar();
        var p = conexao.prepareStatement(
            "INSERT INTO eventos(nome, local, data, descricao, id_usuario) values (?,?,?,?, 1);");
        p.setString(1, nome);
        p.setString(2, local);
        p.setString(3, data);
        p.setString(4, descricao);
        p.execute();
        p.close();
        conexao.close();         
    }
    public boolean existeVerificado(LoginController u) throws Exception {      
        var conexao = new Conexao().conectar();
        var p = conexao.prepareStatement("SELECT * FROM users WHERE email = ? AND  senha = ? AND status_verificacao='Verificado';");
        p.setString(1, u.login);
        p.setString(2, u.senha);
        var rs = p.executeQuery();
        var usuarioExiste = rs.next();
        p.close();
        conexao.close();   
        return usuarioExiste;
    }
    public boolean existeNaoVerificado(LoginController u) throws Exception {      
        var conexao = new Conexao().conectar();
        var p = conexao.prepareStatement("SELECT * FROM users WHERE email = ? AND  senha = ? AND status_verificacao='Não Verificado' limit 1;");
        p.setString(1, u.login);
        p.setString(2, u.senha);
        var rs = p.executeQuery();
        var usuarioExiste = rs.next();
        p.close();
        conexao.close();   
        return usuarioExiste;
    }
        public void verificacaoUsuarioOk(String email) throws SQLException {
        var conexao = new Conexao().conectar();
        var p = conexao.prepareStatement("UPDATE users SET cod_verificacao='', status_verificacao='Verificado' where email=?");
        p.setString(1, email);
        p.execute();
        p.close();
        conexao.close();  
    }
    public boolean verificacaoUsuarioCodigo(String email, String cod_verificacao) throws SQLException {
        boolean verify = false;
        var conexao = new Conexao().conectar();
        var p = conexao.prepareStatement("SELECT * FROM users WHERE email=? and cod_verificacao=? limit 1", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        p.setString(1, email);
        p.setString(2, cod_verificacao);
        ResultSet r = p.executeQuery();
        if (r.first()) {
            verify = true;
        }
        r.close();
        p.close();
        conexao.close();  
        return verify;
    }
    public boolean checkCPFDuplicado(String cpf) throws SQLException {
        boolean duplicate = false;
        var conexao = new Conexao().conectar();
        var p = conexao.prepareStatement("SELECT id FROM users WHERE CPF=? limit 1", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        p.setString(1, cpf);
        ResultSet r = p.executeQuery();
        if (r.first()) {
            duplicate = true;
        }
        r.close();
        p.close();
        conexao.close(); 
        return duplicate;
    }
    public boolean checkEmailDuplicado(String email) throws SQLException {
        boolean duplicate = false;
        var conexao = new Conexao().conectar();
        var p = conexao.prepareStatement("SELECT id FROM users WHERE email=? limit 1", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        p.setString(1, email);
        ResultSet r = p.executeQuery();
        if (r.first()) {
            duplicate = true;
        }
        r.close();
        p.close();
        conexao.close(); 
        return duplicate;
    }
}