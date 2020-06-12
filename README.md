# Projeto de Autoatendimento LicensePlate – AtmLP  

<p align="center"> Status do Projeto: Concluído :heavy_check_mark:</>
<div align="center">   
   <img width="407"  alt="Logo" src="https://user-images.githubusercontent.com/48803004/84099300-a76d9800-a9df-11ea-8173-5701c3b4f8f4.png">
</div>

[![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=23)


## DESCRIÇÃO DO PROJETO
<p align="justify"> O software apresentado é uma ferramenta de auxílio a solicitação de estampagem de placas MERCOSUL, implementado na linguagem Kotlin com especificação para Android. </p>

<p align="justify"> A finalidade da aplicação é auxiliar o atendimento da associação de estampadores que é uma unidade privada, livrando a sociedade da burocracia de ter que procurar áreas de abrangência de cada estamparia, passando ao usuário do aplicativo o conforto de efetuar seu pedido sem precisar sair de sua residência. </p>

<p align="justify">  LicensePlate é a porta de entrada para o cidadão consultar o sistema básico de um estampador, partindo da solicitação de uma nova placa, por meio do qual pode também acessar outros serviços, como verificação de status do processo e retirada do produto da loja, tornando o atendimento mais eficaz. </p>
<p align="justify"><b><a href="https://user-images.githubusercontent.com/48803004/84548533-33333d00-acdc-11ea-95ef-e63d58b01e64.jpeg">Tela Inicial<a></b>.  </p>
   
## MENU E FUNCIONALIDADES
<p align="justify"> O aplicativo LicensePlate é inicializado no menu principal, no qual identificamos o usuário como Reservado ou Solicitante, conforme <b><a href="https://user-images.githubusercontent.com/48803004/84548533-33333d00-acdc-11ea-95ef-e63d58b01e64.jpeg">Tela Inicial<a></b>.  </p>
<p align="justify">O usuário Solicitante terá duas linhas de ação:</p>
<ul>
    <li><b>Solicitação de placa:</b> O sistema permitirá inserção dos dados de autorização e do usuário, na seguinte ordem: estado, nº autorização ou placa, dados pessoais (nome, CPF, telefone). E ainda, a permissão de escolher um estampador mais próximo.</li>
    <li><b>Verificação de solicitação:</b> O sistema permitirá a verificação do status de fabricação da placa pelo número de protocolo.</li>
</ul>  

<p align="justify">O botão da área reservada está disponível para os perfis Fabricante e Estampador. Na <b><a href="https://user-images.githubusercontent.com/48803004/84093070-961c8f80-a9cf-11ea-895f-4dd55bda52d4.png">Tela de Cadastro</a></b> o usuário especificará seu perfil.</p>
<ul>
    <li><b>Perfil Fabricante:</b> Responsável pelo gerenciamento das solicitações de cadastro de estamparias.</li>
    <li><b>Perfil Estampador:</b> O sistema permitirá o gerenciamento (confirmação e conclusão) das solicitações de placas e a consulta de todas as solicitações concluídas.</li>
</ul> 

## FERRAMENTAS

<ul>
   <li><b>Linguagem:</b> Kotlin</li>
   <li><b>Arquitetura:</b> MVVMI (Model – View – View Model - interactor) </li>
   <li><b>Armazenamento de dados:</b> <a href="https://firebase.google.com/docs/auth"> Firebase Realtime Database </a> </li>
   <li><b>Chatbot:</b><a href="https://dialogflow.com/docs"> DialogFlow </a></li>
   <li><b>Publicação do Chatbot:</b><a href="https://signup.heroku.com"> Heroku </a></li>
</ul>

## COMO RODAR A APLICAÇÃO 
<p align="justify"> Para abrir uma das amostras do projeto no Android Studio, comece fazendo checkout de uma das ramificações(Branchs) de amostra e abra o diretório raiz no Android Studio.</p>

### CLONANDO O PROJETO

1. Clone o repositório:

```
git clone https://github.com/jjorge98/LicensePlate.git
```

2. Esta etapa garante que você esteja na ramificação principal. 

```
git checkout master
```

<b>Nota:</b> Se você desejar alterar para uma ramificação(Branch) diferente, substitua "master" pelo nome da ramificação(Branch) que deseja visualizar.

3. Por fim, abra o diretório LicensePlate/ no Android Studio.

### Configurar um dispositivo para execução do aplicativo

1. No dispositivo, abra as configurações, selecione opções do desenvolvedor e ative a depuração USB.

<b>Nota:</b> Se você não encontrar as opções do desenvolvedor, siga as <a href="https://developer.android.com/studio/debug/dev-options">instruções</a> para ativar as opções do desenvolvedor.

2. Conecte o seu dispositivo através do USB, e faça a configuração recomendada pelo Adroid Studio, em seguida para <b>executar</b> o aplicativo clique ```Shift+F10```.

### Configurar um emulador para execução do aplicativo

<b>Nota:</b> Siga o passo a passo da <a href="https://developer.android.com/studio/run/emulator">documentação</a> do Android Studio.

## PLANOS FUTUROS 
<p align="justify"> :trophy: O projeto tem a possibilidade de incluir melhorias evolutivas como:</p>
<ul>
    <li>Funcionalidade de pagamentos</li>
    <li>Agendar emplacamento no local de escolha do cliente.</li>
    <li>Oferecimento de rotas para estamparia selecionada</li>
</ul>  

## DESENVOLVEDORES
<p align="justify"> :octocat: <a href="https://github.com/mariliaalvim"> Marília Alvim </a> </p>
<p align="justify"> :octocat: <a href="https://github.com/MilenaNobre"> Milena Nobre </a> </p>
<p align="justify"> :octocat: <a href="https://github.com/jjorge98"> Jorge Júnior </a> </p>
