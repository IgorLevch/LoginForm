import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarculaLaf;

public class LoginForm extends JFrame {

    final private Font mainFont = new Font("Segoe print", Font.BOLD,18); // the font that ll be used by the diff-t components
    JTextField  tfEmail;
    JPasswordField pfPassword;

        public void initialize(){   // initializing the frame
        
            // Form panel

            JLabel lbLoginForm = new JLabel("Login Form",SwingConstants.CENTER);
            lbLoginForm.setFont(mainFont);

            JLabel lbEmail = new JLabel("Email");
            lbEmail.setFont(mainFont);    

            tfEmail = new JTextField();
            tfEmail.setFont(mainFont);

            JLabel lbPassword = new JLabel("Password");
            lbPassword.setFont(mainFont);

            pfPassword = new JPasswordField();
            pfPassword.setFont(mainFont);

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridLayout(0,1,10,10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
            // 0 - the number of rows is undefined. We can use any numbr of rows.
            // 1 - we have 1 single column
            // 10,10 - the margin between the diff-t components

            // (добавляем компоненты в панель formPanel)

            formPanel.add(lbLoginForm);
            formPanel.add(lbEmail);
            formPanel.add(tfEmail);
            formPanel.add(lbPassword);
            formPanel.add(pfPassword);

            // buttons pannel
            JButton btnLogin = new JButton("Login");
            btnLogin.setFont(mainFont);
            btnLogin.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    String email = tfEmail.getText();
                    String password = String.valueOf(pfPassword.getPassword());
                    User user = getAuthenticatedUser(email,password);
                    
                    if (user!=null) {

                        MainFrame mainFrame = new MainFrame();  // ll be displayed when we click on the login button
                        mainFrame.initialize(user);
                        dispose();
                        
                    } else{

                        JOptionPane.showMessageDialog(LoginForm.this,
                         "Email or Password invalid", 
                         "Try again", 
                         JOptionPane.ERROR_MESSAGE);
                    }

                    
                }
                
            });


            JButton btnCancel = new JButton("Cancel");
            btnCancel.setFont(mainFont);
            btnCancel.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
                
            });

            JPanel buttonsJPanel = new JPanel();
            buttonsJPanel.setLayout(new GridLayout(1,2,10,0));
            buttonsJPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
            buttonsJPanel.add(btnLogin);
            buttonsJPanel.add(btnCancel);



            // Initialize the frame/  добавляем panel во frame (frame by default uses a border layout)
            add(formPanel,BorderLayout.NORTH);
            add(buttonsJPanel, BorderLayout.SOUTH);

        setTitle("LoginForm");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400,500);
        setMinimumSize(new Dimension(350,450));
        setLocationRelativeTo(null);  // in the middle of the screen
        setVisible(true);
        
        
        
        
        }


        private User getAuthenticatedUser(String email, String password){

            User user = null;
            final String DB_URL = "jdbc:mysql://localhost/mystore";
            final String USERNAME = "root";
            final String PASSWORD = "as56df";


            try{

                Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                String sql = "SELECT * FROM users WHERE email=? AND password=?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){

                    user = new User();
                    user.name = resultSet.getString("name");
                    user.email = resultSet.getString("email");
                    user.phone = resultSet.getString("phone");
                    user.address = resultSet.getString("address");
                    user.password = resultSet.getString("password");
                }

                preparedStatement.close();
                conn.close();
            }   catch(Exception e){

                System.out.println("Database connection failed!");
            }

            return user;

        }

        public static void main(String[] args) {

            try {
                UIManager.setLookAndFeel( new FlatDarculaLaf() );
            } catch( Exception ex ) {
                System.err.println( "Failed to initialize LaF" );
            }
            LoginForm loginForm = new LoginForm();
            loginForm.initialize();
        }


}
