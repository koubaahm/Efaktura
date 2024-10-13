import { Role } from "./role";

export class RegisterRequest {
      fullname!:String;
      email!:String;
     password!:String;
     roles !:Role[];
}
