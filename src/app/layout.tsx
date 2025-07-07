import type { Metadata } from "next"
import { Inter, Playfair_Display } from "next/font/google"
import "./globals.css"
import Providers from "@/components/providers"
import Navbar from "@/components/navbar"
import Footer from "@/components/footer"
import CursorTrail from "@/components/cursor-trail"

const inter = Inter({ 
  subsets: ["latin"],
  variable: "--font-inter",
})

const playfair = Playfair_Display({ 
  subsets: ["latin"],
  variable: "--font-playfair",
})

export const metadata: Metadata = {
  title: "Hotel Kalsubai Gate Point - Luxury Mountain Resort",
  description: "Experience luxury and comfort at the gateway to Maharashtra's highest peak. Premium rooms, fine dining, and breathtaking mountain views await you.",
  keywords: "hotel, kalsubai, mountain resort, luxury accommodation, maharashtra, bhandardara",
  authors: [{ name: "Hotel Kalsubai" }],
  openGraph: {
    title: "Hotel Kalsubai Gate Point - Luxury Mountain Resort",
    description: "Experience luxury and comfort at the gateway to Maharashtra's highest peak.",
    type: "website",
    locale: "en_IN",
  },
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className={`${inter.variable} ${playfair.variable} font-sans antialiased`}>
        <Providers>
          <CursorTrail />
          <Navbar />
          <main className="min-h-screen">
            {children}
          </main>
          <Footer />
        </Providers>
      </body>
    </html>
  )
}