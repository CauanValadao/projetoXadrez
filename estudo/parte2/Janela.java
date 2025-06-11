package estudo.parte2;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Janela {
    public static void main(String[] args){
        JFrame janela = new JFrame("testando");

        JButton botao = new JButton("Clique aqui");
        botao.setBounds(0, 0, 200, 30);
        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello world!");
            }
            
        });

        JLabel labelUsuario = new JLabel("Usuario:");
        labelUsuario.setBounds(50,50,100,30);
        
        JTextField campoUsuario = new JTextField();
        campoUsuario.setBounds(50, 80, 100, 30);

        janela.setLayout(null);

        janela.add(botao);
        janela.add(labelUsuario);
        janela.add(campoUsuario);
        janela.setBounds(760,240,400,600);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setVisible(true);

    }
}
