import { Confirmacao } from "./confirmacao.model";
import { Tema } from "./tema.model";
import { User } from "./user.model";

export interface Post {
  id: number;
  data?: string;
  corpo: string;
  imagem: File;
  localizacao?: string;
  cidade?: string;
  pessoa: User;
  tema: Tema;
  distanciaDaPessoaLogada: string;
  confirmadaPeloUsuarioLogado: boolean;
  confirmacaoDeInfo: Confirmacao[];
}
