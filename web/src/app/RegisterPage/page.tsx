import { useState } from "react";
import { FormField } from "../../components/FormField";
import { SubmitButton } from "../../components/SubmitButton";
import { TermsCheckbox } from "../../components/TermsCheckBox";
import { AuthFooterLink } from "../../components/AuthFooterLink";

export default function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [acceptedTerms, setAcceptedTerms] = useState(false);

  return (
    <div className="p-4">
      <form onSubmit={(e) => e.preventDefault()}>
        <h2 className="text-lg font-semibold">Criar Conta</h2>
        <p className="text-textSecondary mb-4">Junte-se ao OrderFlow</p>

        <div className="space-y-4 mb-6">
          <FormField
            id="name"
            label="Nome Completo"
            placeholder="Seu nome"
            value={name}
            onChange={setName}
          />
          <FormField
            id="email"
            label="E-mail"
            type="email"
            placeholder="seu@email.com"
            value={email}
            onChange={setEmail}
          />
          <FormField
            id="password"
            label="Senha"
            type="password"
            placeholder="********"
            value={password}
            onChange={setPassword}
          />
        </div>

        <TermsCheckbox checked={acceptedTerms} onCheckedChange={setAcceptedTerms} />

        <SubmitButton label="Criar Conta" disabled={!acceptedTerms} />

        <AuthFooterLink
          question="Já tem conta?"
          actionLabel="Entrar"
          to="/login"
         />
      </form>
    </div>
  );
}