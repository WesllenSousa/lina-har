package util;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import constants.ConstImagens;

/**
 *
 * @author Wesllen Sousa Lima
 */
public class Messages {

    public void bug(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Error", JOptionPane.ERROR_MESSAGE,
                new ImageIcon(getClass().getResource(ConstImagens.bug)));
    }

    public void sucesso(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Success", JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon(getClass().getResource(ConstImagens.sucesso)));
    }

    public void aviso(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Warming", JOptionPane.WARNING_MESSAGE,
                new ImageIcon(getClass().getResource(ConstImagens.aviso)));
    }

    public boolean confirmacao(String mensagem) {
        int conf = JOptionPane.showConfirmDialog(null, mensagem, "Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(getClass().getResource(ConstImagens.pergunta)));
        return conf == JOptionPane.YES_OPTION;
    }

    public String inserirDados(String mensagem) {
        return (String) JOptionPane.showInputDialog(null, mensagem, "Choose",
                JOptionPane.QUESTION_MESSAGE, new ImageIcon(getClass().getResource(ConstImagens.inserir)),
                null, null);
    }

    public String inserirDadosComValorInicial(String mensagem, String valorInicial) {
        return (String) JOptionPane.showInputDialog(null, mensagem, "Choose",
                JOptionPane.QUESTION_MESSAGE, new ImageIcon(getClass().getResource(ConstImagens.inserir)),
                null, valorInicial);
    }

}
