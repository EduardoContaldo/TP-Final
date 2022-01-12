# Trabalho Prático Final: Internet Banking & Persistência

**Valor: 25 pontos**

**No Integrantes: 2**

O objetivo principal deste trabalho consiste em complementar os mecanismos de persistência para salvar as informações do Internet Banking como um todo.
Para isso, implemente as seguintes soluções abaixo.

## 01. Main.java

Você deverá implementar um arquivo chamado `Main.java` que conterá um menu para acesso as principais funções do sistema.
Especificamente, este menu deverá permitir ao usuário:

1. Criar nova conta: Deverá permitir a criação de uma nova conta bancária. Os dados deverão ser fornecidos durante o procedimento de cadastro.
2. Login como cliente: Realizar login em uma conta bancária de um cliente.
   1. Depósito: Depositar um determinado valor na conta
   2. Saque: Retirar um determinado valor da conta
   3. Transferência: Transferir um determinado valor desta conta para outra a ser fornecida.
   4. Excluir conta: Excluir a conta do cliente
   5. logout
3. Login como administrador: Logar como admin utilizando as seguintes credenciais: login (`admin`), senha (`ib2021!`)
   1. Criar nova agência: Criar uma nova agência
   2. Excluir agência: Excluir uma agência existente. Atenção: também deverão ser excluídas as contas relacionadas a essa agência.
   3. logout
4. Sair

## 02. Persistência dos dados

Você deverá estender os mecanismos de persistência existentes para que todas as ações e dados gerados durante a utilização do sistema do Internet Banking sejam persistidos e, portanto, recuperados durante sua inicialização.
Especificamente, você deverá implementar rotinas para salvar os dados de toda nova conta criada, quando o usuário realizar logout da conta, e quando for necessário sair do programa.

Você pode utilizar o método de armazenamento que considerar mais conveniente, porém é recomendado o uso de serialização.
Nesse caso, você deverá salvar os dados de cada conta em um arquivo separado, dentro de uma pasta `db/contas/`.
O mesmo deve ser feito para agências, porém elas serão salvas dentro do diretório `db/agencias/`.

## Forma de entrega

O trabalho deverá ser entregue por meio de um pull request. Ainda, um vídeo (ou relatório) deverá ser gravado para demonstrar as funcionalidades que foram efetivamente implementadas, bem como descrever as dificuldades encontradas (e soluções adotadas para superá-las), e as limitações que impediram a implementação de alguma funcionalidade do trabalho (se houver).
