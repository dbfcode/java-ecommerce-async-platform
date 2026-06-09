import { useEffect, useState } from "react";

type Category = {
  id: number;
  name: string;
};

type Product = {
  id: number;
  name: string;
  category?: Category;
  price: number;
};

type ProductsResponse = {
  content: Product[];
  totalPages: number;
  totalElements: number;
};

export default function useProducts() {
  const [products, setProducts] = useState<ProductsResponse>({
    content: [],
    totalPages: 0,
    totalElements: 0,
  });

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchProducts() {
      try {
        setLoading(true);

        const res = await fetch("http://localhost:8080/products");

        if (!res.ok) {
          throw new Error("Erro ao buscar produtos");
        }

        const data: ProductsResponse = await res.json();

        console.log("Produtos recebidos:", data);

        setProducts(data);
      } catch (err: unknown) {
        if (err instanceof Error) {
          setError("Não foi possível carregar os produtos");
        } else {
          setError("Erro desconhecido");
        }
      } finally {
        setLoading(false);
      }
    }

    fetchProducts();
  }, []);

  return { products, loading, error };
}