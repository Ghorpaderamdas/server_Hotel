'use client'

import { useEffect, useRef } from 'react'
import { motion, useMotionValue, useSpring } from 'framer-motion'

interface SpoonTrail {
  id: number
  x: number
  y: number
  opacity: number
  scale: number
  rotation: number
}

export default function CursorTrail() {
  const mouseX = useMotionValue(0)
  const mouseY = useMotionValue(0)
  
  const springConfig = { damping: 25, stiffness: 700 }
  const x = useSpring(mouseX, springConfig)
  const y = useSpring(mouseY, springConfig)
  
  const spoonsRef = useRef<SpoonTrail[]>([])
  const animationRef = useRef<number>()
  const containerRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    const handleMouseMove = (e: MouseEvent) => {
      mouseX.set(e.clientX)
      mouseY.set(e.clientY)
      
      // Add new spoon to trail
      const newSpoon: SpoonTrail = {
        id: Date.now(),
        x: e.clientX,
        y: e.clientY,
        opacity: 0.8,
        scale: Math.random() * 0.5 + 0.5,
        rotation: Math.random() * 360
      }
      
      spoonsRef.current.push(newSpoon)
      
      // Limit trail length
      if (spoonsRef.current.length > 15) {
        spoonsRef.current.shift()
      }
    }

    const animate = () => {
      if (containerRef.current) {
        // Update spoon positions and fade them out
        spoonsRef.current = spoonsRef.current
          .map(spoon => ({
            ...spoon,
            opacity: spoon.opacity * 0.95,
            scale: spoon.scale * 0.98
          }))
          .filter(spoon => spoon.opacity > 0.01)

        // Render spoons
        containerRef.current.innerHTML = spoonsRef.current
          .map(spoon => `
            <div 
              class="absolute pointer-events-none transition-all duration-100 ease-out"
              style="
                left: ${spoon.x - 12}px;
                top: ${spoon.y - 12}px;
                opacity: ${spoon.opacity};
                transform: scale(${spoon.scale}) rotate(${spoon.rotation}deg);
              "
            >
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2C12.5523 2 13 2.44772 13 3V11H15C16.1046 11 17 11.8954 17 13V21C17 21.5523 16.5523 22 16 22C15.4477 22 15 21.5523 15 21V13H13V21C13 21.5523 12.5523 22 12 22C11.4477 22 11 21.5523 11 21V13H9V21C9 21.5523 8.44772 22 8 22C7.44772 22 7 21.5523 7 21V13C7 11.8954 7.89543 11 9 11H11V3C11 2.44772 11.4477 2 12 2Z" fill="url(#spoonGradient)" fill-opacity="0.6"/>
                <defs>
                  <linearGradient id="spoonGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                    <stop offset="0%" style="stop-color:#f59e0b;stop-opacity:1" />
                    <stop offset="100%" style="stop-color:#d97706;stop-opacity:1" />
                  </linearGradient>
                </defs>
              </svg>
            </div>
          `)
          .join('')
      }
      
      animationRef.current = requestAnimationFrame(animate)
    }

    window.addEventListener('mousemove', handleMouseMove)
    animationRef.current = requestAnimationFrame(animate)

    return () => {
      window.removeEventListener('mousemove', handleMouseMove)
      if (animationRef.current) {
        cancelAnimationFrame(animationRef.current)
      }
    }
  }, [mouseX, mouseY])

  return (
    <>
      {/* Main cursor follower */}
      <motion.div
        className="fixed top-0 left-0 w-6 h-6 pointer-events-none z-50 mix-blend-difference"
        style={{ x, y }}
      >
        <div className="w-full h-full bg-white rounded-full opacity-80 blur-sm" />
      </motion.div>
      
      {/* Spoon trail container */}
      <div
        ref={containerRef}
        className="fixed inset-0 pointer-events-none z-40"
        style={{ mixBlendMode: 'multiply' }}
      />
    </>
  )
}