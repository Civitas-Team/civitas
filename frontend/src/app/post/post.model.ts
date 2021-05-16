import { Tema } from "./tema.model";
import { User } from "./user.model";

export interface Post {
  data?: string;
  corpo: string;
  imagem: File;
  localizacao?: string;
  pessoa: User;
  tema: Tema;
  distanciaDaPessoaLogada: string;
}
