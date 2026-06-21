interface TermsCheckboxProps {
  checked: boolean;
  onCheckedChange: (checked: boolean) => void;
}

export function TermsCheckbox({ checked, onCheckedChange }: TermsCheckboxProps) {
  return (
    <div className="flex items-center gap-1 mb-2">
      <input
        id="terms"
        type="checkbox"
        checked={checked}
        onChange={(e) => onCheckedChange(e.target.checked)}
        className="h-3 w-3 cursor-pointer rounded border-border accent-text"
      />
      <label htmlFor="terms" className="text-[12px] text-textSecondary cursor-pointer">
        Concordo com os termos de uso
      </label>
    </div>
  );
}